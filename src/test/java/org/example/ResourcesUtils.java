package org.example;

import com.google.common.io.Resources;
import com.regnosys.rosetta.common.hashing.ReferenceResolverProcessStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import org.isda.cdm.processor.CdmReferenceConfig;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResourcesUtils {

    public static String getJson(String resourceName) throws IOException {
        URL url = Resources.getResource(resourceName);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }

    public static String toJson(RosettaModelObject object) throws IOException {
        return RosettaObjectMapper.getNewRosettaObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);
    }

    public static <T extends RosettaModelObject> T getObject(Class<T> clazz, String resourceName) throws IOException {
        String json = getJson(resourceName);
        return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, clazz);
    }

    public static <T extends RosettaModelObject> T getObjectAndResolveReferences(Class<T> clazz, String resourceName) throws IOException {
        T object = getObject(clazz, resourceName);
        return resolveReferences(object);
    }

    private static <T extends RosettaModelObject> T resolveReferences(T object) {
        RosettaModelObject builder = object.toBuilder();
        new ReferenceResolverProcessStep(CdmReferenceConfig.get()).runProcessStep(builder.getType(), builder);
        return (T) builder.build();
    }
}