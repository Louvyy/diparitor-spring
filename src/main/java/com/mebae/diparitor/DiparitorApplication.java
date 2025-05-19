package com.mebae.diparitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Diparitor.
 * Cette classe contient la méthode `main` qui lance l'application Spring Boot. Elle est annotée
 * avec {@link SpringBootApplication}, ce qui signifie qu'elle est responsable de la
 * configuration de l'application et du démarrage du contexte Spring.
 *
 * @author Lionia ROMAS
 * @version 1.0
 */
@SpringBootApplication
public class DiparitorApplication {
  public static void main(final String[] args) {
    SpringApplication.run(DiparitorApplication.class, args);
  }
}
