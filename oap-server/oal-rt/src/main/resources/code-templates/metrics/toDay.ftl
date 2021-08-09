public org.apache.skywalking.oap.server.core.analysis.metrics.Metrics toDay() {
${metricsClassPackage}${metricsName}Metrics metrics = (${metricsClassPackage}${metricsName}Metrics) org.apache.skywalking.oap.server.core.MetricsObjectPool.get(${metricsClassPackage}${metricsName}Metrics.class);
<#list fieldsFromSource as field>
    <#if field.columnName == "time_bucket">
        metrics.setTimeBucket(toTimeBucketInDay());
    <#elseif field.typeName == "java.lang.String" || field.typeName == "long" || field.typeName == "int" || field.typeName == "double" || field.typeName == "float">
        metrics.${field.fieldSetter}(this.${field.fieldGetter}());
    <#else>
        ${field.typeName} newValue = new ${field.typeName}();
        newValue.copyFrom(this.${field.fieldGetter}());
        metrics.${field.fieldSetter}(newValue);
    </#if>
</#list>
<#list persistentFields as field>
    <#if field.columnName == "time_bucket">
        metrics.setTimeBucket(toTimeBucketInDay());
    <#elseif field.typeName == "java.lang.String" || field.typeName == "long" || field.typeName == "int" || field.typeName == "double" || field.typeName == "float">
        metrics.${field.fieldSetter}(this.${field.fieldGetter}());
    <#else>
        ${field.typeName} newValue = new ${field.typeName}();
        newValue.copyFrom(this.${field.fieldGetter}());
        metrics.${field.fieldSetter}(newValue);
    </#if>
</#list>
return metrics;
}
