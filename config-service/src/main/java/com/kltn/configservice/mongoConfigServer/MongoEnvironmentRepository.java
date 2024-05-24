package com.kltn.configservice.mongoConfigServer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EnvironmentRepository} that is backed by MongoDB. The
 * resulting {@link Environment} is composed of property sources where the
 * {@literal application-name} is identified by the collection's {@literal name} and a
 * MongoDB document's {@literal profile} and {@literal label} values represent the Spring
 * application's {@literal profile} and {@literal label} respectively. All properties must
 * be listed under the {@literal source} key of the document.
 *
 * @author Venil Noronha
 */
public class MongoEnvironmentRepository implements EnvironmentRepository {

    private static final String LABEL = "label";
    private static final String PROFILE = "profile";
    private static final String DEFAULT = "default";
    private static final String DEFAULT_PROFILE = null;
    private static final String DEFAULT_LABEL = "";

    private final MongoTemplate mongoTemplate;
    private final MapFlattener mapFlattener;

    public MongoEnvironmentRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.mapFlattener = new MapFlattener();
    }

    @Override
    public Environment findOne(String application, String profile, String label) {
        List<String> applicationList = Arrays.stream(application.split(",")).map(String::trim).toList();

        String[] profilesArr = StringUtils.commaDelimitedListToStringArray(profile);
        List<String> profiles = new ArrayList<String>(Arrays.asList(profilesArr.clone()));
        for (int i = 0; i < profiles.size(); i++) {
            if (DEFAULT.equals(profiles.get(i))) {
                profiles.set(i, DEFAULT_PROFILE);
            }
        }
        if(profile == null || profile.isEmpty()) {
            profiles.add(DEFAULT_PROFILE); // Default configuration will have 'null' profile
        }
        profiles = sortedUnique(profiles);

        List<String> labels = Arrays.asList(label, DEFAULT_LABEL); // Default configuration will have 'null' label
        labels = sortedUnique(labels);

        Query query = new Query();
        query.addCriteria(Criteria.where(PROFILE).in(profiles.toArray()));
        query.addCriteria(Criteria.where(LABEL).in(labels.toArray()));

        Environment environment;
        try {
//            List<MongoPropertySource> sources = mongoTemplate.find(query, MongoPropertySource.class, application);
            List<MongoPropertySource> sources = applicationList.stream()
                    .flatMap(appName -> mongoTemplate.find(query, MongoPropertySource.class, appName).stream())
                    .collect(Collectors.toList());
            sortSourcesByLabel(sources, labels);
            sortSourcesByProfile(sources, profiles);
            environment = new Environment(application, profilesArr, label, null, null);
            int index = 0;
            for (MongoPropertySource propertySource : sources) {
                String sourceName = generateSourceName(applicationList.get(index), propertySource);
                Map<String, Object> flatSource = mapFlattener.flatten(propertySource.getSource());
                PropertySource propSource = new PropertySource(sourceName, flatSource);
                environment.add(propSource);
                index++;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load environment", e);
        }

        return environment;
    }

    private ArrayList<String> sortedUnique(List<String> values) {
        return new ArrayList<String>(new LinkedHashSet<String>(values));
    }

    private void sortSourcesByLabel(List<MongoPropertySource> sources,
                                    final List<String> labels) {
        sources.sort(new Comparator<MongoPropertySource>() {

            @Override
            public int compare(MongoPropertySource s1, MongoPropertySource s2) {
                int i1 = labels.indexOf(s1.getLabel());
                int i2 = labels.indexOf(s2.getLabel());
                return Integer.compare(i1, i2);
            }

        });
    }

    private void sortSourcesByProfile(List<MongoPropertySource> sources,
                                      final List<String> profiles) {
        sources.sort(new Comparator<MongoPropertySource>() {

            @Override
            public int compare(MongoPropertySource s1, MongoPropertySource s2) {
                int i1 = profiles.indexOf(s1.getProfile());
                int i2 = profiles.indexOf(s2.getProfile());
                return Integer.compare(i1, i2);
            }

        });
    }

    private String generateSourceName(String environmentName, MongoPropertySource source) {
        String sourceName;
        String profile = source.getProfile() != null ? source.getProfile() : DEFAULT;
        String label = source.getLabel();
        if (label != null) {
            sourceName = String.format("%s-%s-%s", environmentName, profile, label);
        } else {
            sourceName = String.format("%s-%s", environmentName, profile);
        }
        return sourceName;
    }

    @Setter @Getter
    public static class MongoPropertySource {

        private String profile;
        private String label;
        private LinkedHashMap<String, Object> source = new LinkedHashMap<String, Object>();
    }

    private static class MapFlattener extends YamlProcessor {

        public Map<String, Object> flatten(Map<String, Object> source) {
            return getFlattenedMap(source);
        }

    }

}