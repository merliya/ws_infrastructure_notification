package com.jbhunt.infrastructure.notification.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ProdCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return "PROD".equalsIgnoreCase(context.getEnvironment().getProperty("runtime.environment"));
    }
}
