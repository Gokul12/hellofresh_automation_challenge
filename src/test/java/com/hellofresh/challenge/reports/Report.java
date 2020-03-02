package com.hellofresh.challenge.reports;

import com.hellofresh.challenge.commons.LoggerClass;
import com.hellofresh.challenge.commons.TestListener;
import com.hellofresh.challenge.config.SuiteProperties;
import com.hellofresh.challenge.enums.Suite;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import static com.hellofresh.challenge.constants.Constant.REPORT_FILE_NAME;
import static com.hellofresh.challenge.constants.Constant.RESOURCES_PATH;

public abstract class Report {

  private int passCount;
  private int failCount;
  private int skipCount;
  private long totalDurationInSeconds;
  private long startTime;
  private String templateFile;
  private Transformer transformer;
  private HashMap<String, String> commonAttrs;
  int counter = 0;
  Document doc;
  DocumentBuilder docBuilder;

  Report(String templateFile, String platform) {
    this.templateFile = templateFile;
    try {
      SuiteProperties suiteProperties = TestListener.SUITE_PROPERTIES;
      String browser = suiteProperties.getBrowser().toUpperCase();
      String environment = suiteProperties.getEnvironment().toUpperCase();
      commonAttrs = new HashMap<>();
      totalDurationInSeconds = 0;
      commonAttrs.put("OS Version", System.getProperty("os.name"));
      commonAttrs.put("SUITE NAME", "SANITY");
      commonAttrs.put("PASS COUNT", "0");
      commonAttrs.put("FAIL COUNT", "0");
      commonAttrs.put("PASS WITH WARNINGS COUNT", "0");
      commonAttrs.put("SKIP COUNT", "0");
      commonAttrs.put("TOTAL COUNT", "0");
      commonAttrs.put("DURATION_IN_MINS", "0");
      commonAttrs.put("ENVIRONMENT", environment);
      if (platform.equalsIgnoreCase(Suite.WEB.name())) {
        commonAttrs.put("BROWSER", browser);
      } else {
        commonAttrs.put("BROWSER", "NA");
      }
      commonAttrs.put("PLATFORM", platform);
      startTime = System.currentTimeMillis();
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      docBuilder = dbf.newDocumentBuilder();
      doc = docBuilder.parse(this.templateFile);
      this.transformer = initTransformer();
    } catch (Exception e) {
      LoggerClass.logError("Error in initializing the Report", e);
    }
  }

  public abstract void addResults(TestResult tr);

  private void addSummaryItem(String key, String value) {
    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//table[@id=\"Summary\"]/tbody/tr/th";
    NodeList nodeList;
    try {
      nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

      for (int i = 0; i < nodeList.getLength(); i++) {

        if (nodeList.item(i).getTextContent().equalsIgnoreCase(key)) {
          Element tdNode = doc.createElement("td");
          tdNode.setTextContent(value);

          nodeList.item(i).getParentNode().insertBefore(tdNode, nodeList.item(i).getNextSibling());

          if ("td".equals(nodeList.item(i).getNextSibling().getNodeName())) {
          }
        }
      }
      copyContent();

    } catch (Exception e) {
      LoggerClass.log(e);
    }
  }

  void generateSummary() {
    for (Map.Entry<String, String> pair : commonAttrs.entrySet()) {
      addSummaryItem(pair.getKey(), pair.getValue());
    }
  }

  private void updateCount(String category) {
    int currentCount = Integer.parseInt(commonAttrs.get(category));
    updateSummaryItem(category, String.valueOf(currentCount + 1));
    commonAttrs.put(category, String.valueOf(currentCount + 1));
  }

  private void updateSummaryItem(String key, String value) {
    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//table[@id=\"Summary\"]/tbody/tr/th";
    NodeList nodeList;
    try {
      nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
      for (int i = 0; i < nodeList.getLength(); i++) {
        if (nodeList.item(i).getTextContent().equalsIgnoreCase(key)) {
          Node node = nodeList.item(i).getNextSibling();
          while (!"td".equalsIgnoreCase(node.getNodeName())) {
            node = node.getNextSibling();
          }
          node.setTextContent(value);
        }
      }
      copyContent();
    } catch (Exception e) {
      LoggerClass.log(e);
    }
  }

  synchronized void updateTestStatus(TestResult testResult) {
    updateCount("TOTAL COUNT");
    if (TestStatus.PASS.name().equalsIgnoreCase(testResult.getStatus())) {
      passCount++;
      updateCount("PASS COUNT");
    } else if (TestStatus.FAIL.name().equals(testResult.getStatus())) {
      failCount++;
      updateCount("FAIL COUNT");
    } else if (TestStatus.PASS_WITH_WARNINGS.name().equals(testResult.getStatus())) {
      passCount++;
      updateCount("PASS WITH WARNINGS COUNT");
    } else if (TestStatus.SKIP.name().equalsIgnoreCase(testResult.getStatus())) {
      skipCount++;
      updateCount("SKIP COUNT");
    }
    totalDurationInSeconds = (System.currentTimeMillis() - startTime) / 1000;
    String totalTime =
        String.valueOf(totalDurationInSeconds / 60) + ":" + totalDurationInSeconds % 60;
    updateSummaryItem("DURATION_IN_MINS", totalTime);
    commonAttrs.put("DURATION_IN_MINS", totalTime);
  }

  private Transformer initTransformer() {
    Transformer trans = null;
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      trans = transformerFactory.newTransformer();
    } catch (Exception e) {
      LoggerClass.log(e);
    }
    return trans;
  }

  void copyContent() {
    try {
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(RESOURCES_PATH + REPORT_FILE_NAME));
      transformer.transform(source, result);
    } catch (Exception e) {
      LoggerClass.log(e);
    }
  }
}
