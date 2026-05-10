package com.upm.gym.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.upm.gym.model.Membership;
import com.upm.gym.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ReceiptController {

    @FXML private Label referenceLabel;
    @FXML private Label memberLabel;
    @FXML private Label memberIdLabel;
    @FXML private Label planLabel;
    @FXML private Label startLabel;
    @FXML private Label expiryLabel;
    @FXML private Label cardLabel;
    @FXML private Label timestampLabel;
    @FXML private Label amountLabel;
    @FXML private ImageView qrImage;
    @FXML private Label qrCaption;
    @FXML private Label messageLabel;

    private User loggedInUser;
    private Membership membership;
    private double amount;
    private String last4;
    private String referenceNumber;

    public void setUser(User user) {
        this.loggedInUser = user;
    }

    public void setReceiptData(Membership membership, double amount, String last4) {
        this.membership = membership;
        this.amount = amount;
        this.last4 = last4;
        this.referenceNumber = generateReference();
        populateFields();
        generateQrCode();
    }

    private String generateReference() {
        // Short readable reference like "RCP-A1B2C3"
        return "RCP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private void populateFields() {
        referenceLabel.setText("Reference: " + referenceNumber);
        memberLabel.setText(loggedInUser.getFullName());
        memberIdLabel.setText(loggedInUser.getUserId());
        planLabel.setText(membership.getMembershipType());
        startLabel.setText(membership.getStartDate().toString());
        expiryLabel.setText(membership.getExpiryDate().toString());
        cardLabel.setText("•••• " + last4);
        timestampLabel.setText(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        amountLabel.setText("SAR " + amount);
        qrCaption.setText(referenceNumber);
    }

    private void generateQrCode() {
        try {
            String qrContent = "UPM-GYM|" + referenceNumber +
                    "|" + loggedInUser.getUserId() +
                    "|" + membership.getMembershipType() +
                    "|" + membership.getExpiryDate();

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);

            Image image = new Image(new ByteArrayInputStream(output.toByteArray()));
            qrImage.setImage(image);

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Could not generate QR code.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        try {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job == null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("No printer available.");
                return;
            }

            Node sourceNode = (Node) event.getSource();
            Node sceneRoot = sourceNode.getScene().getRoot();

            boolean proceed = job.showPrintDialog(sourceNode.getScene().getWindow());
            if (!proceed) return;

            boolean printed = job.printPage(sceneRoot);
            if (printed) {
                job.endJob();
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Receipt sent to printer.");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Printing failed. Please try again.");
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Print error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDone(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MemberDashboard.fxml"));
            Parent root = loader.load();

            MemberDashboardController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("UPM Gym - Member Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}