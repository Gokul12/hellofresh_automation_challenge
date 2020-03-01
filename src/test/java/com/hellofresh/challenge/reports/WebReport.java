package com.hellofresh.challenge.reports;

import com.hellofresh.challenge.commons.LoggerClass;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class WebReport extends Report {

  public WebReport(String templateFile) {
    super(templateFile);
    generateSummary();
  }

  private Node addDetailedLogs(List<String> detailedlogs) {
    Element tdNode = doc.createElement("td");

    String showLogsHTML =
        "<div class=\"showlog\" > <b data-toggle=\"modal\" data-target=" + "\"" + "#myModal"
            + String.valueOf(counter) + "\"" + "> Show Logs</b> </div>";
    Node showLogs = generateNodeFromString(showLogsHTML);
    tdNode.appendChild(showLogs);

    try {
      List<String> modalList = new ArrayList<>();
      modalList.add(
          "<div class=\"modal fade\" id=" + "\"" + "myModal" + String.valueOf(counter) + "\"" + " "
              + "tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\"> </div>");
      modalList.add("<div class=\"modal-dialog\" role=\"document\"> </div>");
      modalList.add("<div class=\"modal-content\"> </div>");
      modalList.add(" <div class=\"modal-header\">"
          + " <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&#215;</span></button>"
          + "<h4 class=\"modal-title\" id=\"myModalLabel\">Detailed Log</h4> </div>");
      modalList.add("<div class=\"modal-body\"> </div>");
      Node modalBody = generateModal(tdNode, modalList);
      for (int i = 0; i < detailedlogs.size(); i++) {
        modalBody.appendChild(generateLogEntry(detailedlogs.get(i), (i + 1), "blue"));
      }
    } catch (Exception e) {
      LoggerClass.log(e);
    }
    return tdNode;
  }

  private Node addDuration(String string) {
    Element tdNode = doc.createElement("td");
    tdNode.setTextContent(String.valueOf(string));
    return tdNode;
  }


  private Node addFailedPoints(List<String> failedCheckPoints) {
    Element tdNode = doc.createElement("td");
    for (int i = 0; i < failedCheckPoints.size(); i++) {
      tdNode.appendChild(generateLogEntry(failedCheckPoints.get(i), (i + 1), "red"));
    }
    return tdNode;
  }

  private Element addPasedChkPoints(List<String> passedCheckPoints) {
    Element tdNode = doc.createElement("td");
    for (int i = 0; i < passedCheckPoints.size(); i++) {
      tdNode.appendChild(generateLogEntry(passedCheckPoints.get(i), (i + 1), "green"));
    }
    return tdNode;
  }

  @Override
  public void addResults(TestResult tr) {
    counter++;
    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//table[@id=\"myTable\"]/tbody";
    NodeList nodeList;
    Node tBody = null;
    Element tdNode = doc.createElement("tr");
    tdNode.setAttribute("Class", tr.getStatus());
    tdNode.appendChild(addSerialNumber(counter));
    tdNode.appendChild(addTestScriptName(tr.getTestScriptName()));
    //TODO add test data later
    tdNode.appendChild(addTestData(tr.gettestData()));
    tdNode.appendChild(addStatus(tr.getStatus()));
    tdNode.appendChild(addDuration(tr.getDuration()));
    tdNode.appendChild(addPasedChkPoints(tr.getPassedCheckPoints()));
    tdNode.appendChild(addFailedPoints(tr.getFailedCheckPoints()));
    tdNode.appendChild(addDetailedLogs(tr.getDetailedLogs()));
    tdNode.appendChild(addScreenshot(tr));
    try {
      nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
      tBody = nodeList.item(0);
      tBody.appendChild(tdNode);
    } catch (Exception e) {
      LoggerClass.log(e);
    }
    updateTestStatus(tr);
    copyContent();
  }

  private Node addScreenshot(TestResult testResult) {
    String screenshotPath = testResult.getScreenshotLocation();
    Element tdNode = doc.createElement("td");
    if (screenshotPath != null && !screenshotPath.equals("")) {
      String id = "myImgModel" + counter;
      String showScreenshotString =
          "<div><img data-target=\"#" + id + "\" data-toggle=\"modal\" height=\"120\" src=\""
              + screenshotPath + "\" width=\"160\"/></div>";
      Node showScreenshots = generateNodeFromString(showScreenshotString);
      tdNode.appendChild(showScreenshots);

      String modalString = "<div aria-labelledby=\"myModalLabel\" class=\"modal fade\" id=\"" + id
          + "\" role=\"dialog\" tabindex=\"-1\"><div class=\"modal-dialog\" role=\"document\" style=\"width: 90%;  height: 80%\"><div class=\"modal-content\"><div class=\"modal-header\"><button aria-label=\"Close\" class=\"close\" data-dismiss=\"modal\" type=\"button\"><span aria-hidden=\"true\"></span></button><div class=\"modal-body\"><center><img src=\""
          + screenshotPath + "\"/></center></div></div></div></div></div>";
      Node model = generateNodeFromString(modalString);
      tdNode.appendChild(model);
    }

    return tdNode;
  }

  private Node addSerialNumber(int number) {
    Element tdNode = doc.createElement("td");
    tdNode.setTextContent(String.valueOf(number));
    return tdNode;
  }

  private Node addStatus(String status) {
    Element tdNode = doc.createElement("td");
    tdNode.setTextContent(status);
    return tdNode;
  }


  private Node addTestData(Map<String, String> testData) {
    Element tdNode = doc.createElement("td");
    String textContent = "";
    if (testData != null) {
      for (Map.Entry<String, String> entry : testData.entrySet()) {
        try {
          textContent = textContent + entry.getKey() + ":" + entry.getValue() + "\n\n";
          if (textContent.equals("sampleKey:sampleValue\n\n")) {
            textContent = " ";
          }
        } catch (ClassCastException e) {
          textContent = textContent + entry.getKey() + ":" + entry.getValue() + "\n\n";
        }
        tdNode.setTextContent(textContent);
      }
    }
    return tdNode;
  }


  private Node addTestScriptName(String testScriptName) {
    Element tdNode = doc.createElement("td");
    tdNode.setTextContent(testScriptName);
    return tdNode;
  }


  private Node generateLogEntry(String entry, int seq, String color) {
    Node node = doc.createElement("br");
    try {
      String passEntry =
          "<font color=\"" + color + "\" style= \"display: block;\"><b> " + seq + ". </b>" + entry
              + "</font>";
      Document doc2 = docBuilder.parse(new ByteArrayInputStream(passEntry.getBytes()));

      node = doc.importNode(doc2.getDocumentElement(), true);
    } catch (Exception e) {
      LoggerClass.log(e);
    }

    return node;
  }

  private Node generateModal(Node parentNode, List<String> modalList) {
    for (String modalItem : modalList) {
      Node node = generateNodeFromString(modalItem);
      parentNode.appendChild(node);
      parentNode = node;
    }
    return parentNode;
  }


  private Node generateNodeFromString(String html) {
    Node node = doc.createElement("br");
    try {
      Document doc2 = docBuilder.parse(new ByteArrayInputStream(html.getBytes()));
      node = doc.importNode(doc2.getDocumentElement(), true);
    } catch (Exception e) {
      LoggerClass.log(e);
    }
    return node;
  }
}
