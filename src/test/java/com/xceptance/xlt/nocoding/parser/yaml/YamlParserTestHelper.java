package com.xceptance.xlt.nocoding.parser.yaml;

import java.io.StringReader;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;

public class YamlParserTestHelper
{
    public static Node parseToNode(final String yamlSpec)
    {
        final Yaml yaml = new Yaml();
        return yaml.compose(new StringReader(yamlSpec));
        // return ((MappingNode) root).getValue().get(0).getValueNode();
    }

}
