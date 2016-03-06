/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.interface21.data;

import org.ameba.Constants;
import org.ameba.http.MultiTenantSessionFilter;
import org.ameba.tenancy.TenantHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A TOperatorApplication is the Spring Boot starter app.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
@EnableMongoRepositories
public class TOperatorApplication implements ServletContextInitializer {

    @Bean
    public FilterRegistrationBean myFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new MultiTenantSessionFilter());
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }

    public static void main(String[] args) {
        SpringApplication.run(TOperatorApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setAttribute(Constants.PARAM_MULTI_TENANCY_ENABLED, "true");
    }
}

@RestController
class StampletController {

    @Autowired
    StampletsRepository repo;

    @RequestMapping(value = "/stamplets/{id}", method = RequestMethod.GET)
    public Stamplet getStamplet(@PathVariable String id) {
        return repo.findByUuid(id);
    }
}

@Component
class CLR implements CommandLineRunner {

    @Autowired
    StampletsRepository repo;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {

        TenantHolder.setCurrentTenant("A");
        Collection<Stamplet> astamps = IntStream.range(1, 2000).mapToObj(s -> new Stamplet(Integer.toString(s))).collect(Collectors.toList());
        repo.save(astamps);
        accountRepository.save(new Account("A", astamps));

        TenantHolder.setCurrentTenant("B");
        Collection<Stamplet> bstamps = IntStream.range(1, 2000).mapToObj(s -> new Stamplet(Integer.toString(s))).collect(Collectors.toList());
        repo.save(bstamps);
        accountRepository.save(new Account("B", bstamps));
    }
}

interface AccountRepository extends MongoRepository<Account, String> {}
interface StampletsRepository extends MongoRepository<Stamplet, String> {
    Stamplet findByUuid(String id);
}