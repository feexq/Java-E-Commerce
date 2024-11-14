package com.project.gamemarket.featuretoggle.aspect;

import com.project.gamemarket.featuretoggle.FeatureToggles;
import com.project.gamemarket.featuretoggle.annotation.FeatureToggle;
import com.project.gamemarket.service.exception.FeatureNotEnabledException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import com.project.gamemarket.featuretoggle.FeatureToggleService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    @Around(value = "@annotation(featureToggle)")
    public Object checkFeatureToggleAnnotation(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        return checkToggle(joinPoint, featureToggle);
    }

    @Before(value = "@annotation(featureToggle)")
    public void logMethodCall(JoinPoint joinPoint, FeatureToggle featureToggle) {
        FeatureToggles toggle = featureToggle.value();
        Object[] args = joinPoint.getArgs();
        log.info("Calling method: {} with args: {} and feature toggle: {}", joinPoint.getSignature().getName(), args, toggle.getFeatureName());
    }

    private Object checkToggle(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        FeatureToggles toggle = featureToggle.value();
        if (featureToggleService.isFeatureEnabled(toggle.getFeatureName())) {
            return joinPoint.proceed();
        }
        log.warn("Feature toggle {} is not enabled!", toggle.getFeatureName());
        throw new FeatureNotEnabledException(toggle.getFeatureName());
    }
}
