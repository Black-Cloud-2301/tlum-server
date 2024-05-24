db.createUser({
    user: "sa",
    pwd: "admin",
    roles: [
        { role: "readWrite", db: "config-service" }
    ]
});
