package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Dotenv dotenv = Dotenv.load();
            String dbHost = dotenv.get("DB_HOST");
            String dbPort = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            String dbUser = dotenv.get("DB_USER");
            String dbPassword = dotenv.get("DB_PASSWORD");

            String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

            return new Configuration()
                    .configure()
                    .setProperty("hibernate.connection.url", jdbcUrl)
                    .setProperty("hibernate.connection.username", dbUser)
                    .setProperty("hibernate.connection.password", dbPassword)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Errore nella creazione della SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        System.out.println("Connessione a PostgreSQL riuscita!");
        session.close();
    }
}
