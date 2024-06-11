package com.kltn.individualservice.annotation;

import com.kltn.individualservice.service.RoleService;
import com.kltn.individualservice.util.WebUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class RoleAspect {

    private final WebUtil webUtil;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final RoleService roleService;

    @Before("@within(com.kltn.individualservice.annotation.FunctionPermission)")
    public void functionPermissionCheck(JoinPoint jp) {
        FunctionPermission anno = jp.getTarget().getClass().getAnnotation(FunctionPermission.class);
        roleService.checkFunctionPermission(webUtil.getRoles(), anno.value());
    }

    @Before("@annotation(com.kltn.individualservice.annotation.ActionPermission) && @annotation(anno)")
    public void actionPermissionCheck(JoinPoint jp, ActionPermission anno) {
        FunctionPermission classAnno = jp.getTarget().getClass().getAnnotation(FunctionPermission.class);
        if (classAnno == null) {
            LOGGER.warn("Wrong config permission for class {}", jp.getTarget().getClass());
            return;
        }
        roleService.checkActionPermission(webUtil.getRoles(), anno.value(), classAnno.value());
    }
}
