import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.Node

class XmlHelper {

    private static final String DELIMITER = "/"

    private Set<String> nodes = new TreeSet<>()
    private Set<String> attrs = new TreeSet<>()

    void handleNode(def node, def name) {
        if (hasNode(node)) {
            name += node.name() + DELIMITER
            nodes.add(name)
            if (hasAttributes(node)) {
                node.attributes().each { k, v -> attrs.add("$name${k}") }
            }
            node.children().each({ handleNode(it, name) })
        }
    }

    private boolean hasAttributes(node) {
        node.attributes().size() > 0
    }

    private boolean hasNode(node) {
        node && node instanceof Node
    }

    Set<String> getNodes() {
        return nodes
    }

    Set<String> getAttrs() {
        return attrs
    }

    static void main(String[] args) {
        def stream = XmlHelper.classLoader.getResourceAsStream("PathToNode.xml")
        GPathResult xml = new XmlSlurper().parse(stream)
        def helper = new XmlHelper()
        helper.handleNode(xml.nodeIterator().next(), DELIMITER)
        println "nodes(size:${helper.nodes.size()}):"
        helper.nodes.each { println it }
        println "attributes(size:${helper.attrs.size()}):"
        helper.attrs.each { println it }
    }
}