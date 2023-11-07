package com.kristileka.springooptemplate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableJpaRepositories
class SpringOopTemplateApplication

fun main(args: Array<String>) {
    runApplication<SpringOopTemplateApplication>(*args)
}

