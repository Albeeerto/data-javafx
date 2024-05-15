package com.empresa.datastructures_javafx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {

    @FXML
    private TextField txt_dato;
    @FXML
    private TextArea area_datos;
    @FXML
    private TextField txt_datos_set;
    @FXML
    private TextArea area_datos_set;
    @FXML
    private Label lbl_numero_elementos;
    @FXML
    private Label lbl_fecha_hora;
    @FXML
    private ListView<String> listView;

    private ObservableList<String> datosSet = FXCollections.observableArrayList();

    @FXML
    protected void agregarAL() {
        String nuevoDato = txt_dato.getText();
        if (!nuevoDato.isEmpty()) {
            datosSet.add(nuevoDato);
            txt_dato.clear();
            actualizarNumeroElementos();
        }
    }

    @FXML
    protected void agregarSET() {
        String nuevoDato = txt_datos_set.getText();
        if (!nuevoDato.isEmpty() && !datosSet.contains(nuevoDato)) {
            datosSet.add(nuevoDato);
            txt_datos_set.clear();
            actualizarNumeroElementos();
        }
    }

    @FXML
    protected void eliminarElemento() {
        String elemento = listView.getSelectionModel().getSelectedItem();
        if (elemento != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText("¿Seguro que quieres eliminar este elemento?");
            alert.setContentText(elemento);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    datosSet.remove(elemento);
                    actualizarNumeroElementos();
                }
            });
        }
    }

    @FXML
    protected void mostrarAL() {
        StringBuilder sb = new StringBuilder();
        for (String item : datosSet) {
            sb.append(item);
            sb.append("\n");
        }
        area_datos.setText(sb.toString());
    }

    @FXML
    protected void mostrarSET() {
        // Mostrar la dirección IP privada en un Alert
        try {
            String ip = getLocalIPAddress();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dirección IP");
            alert.setHeaderText("Estás trabajando desde la siguiente IP:");
            alert.setContentText(ip);
            alert.showAndWait();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        // Mostrar la fecha y hora actual en una Label
        mostrarFechaHoraActual();

        // Mostrar los elementos en la ListView
        listView.setItems(FXCollections.observableArrayList(datosSet));
        actualizarNumeroElementos();
    }

    @FXML
    protected void ordenarAlfabeticamente() {
        // Ordenar los elementos alfabéticamente
        Collections.sort(datosSet);
        // Actualizar la ListView
        listView.setItems(FXCollections.observableArrayList(datosSet));
        actualizarNumeroElementos();
    }

    @FXML
    protected void initialize() {
        listView.setItems(datosSet);
        // Inicializar el temporizador para actualizar la fecha y hora cada minuto
        mostrarFechaHoraActual(); // Mostrar la fecha y hora al inicio
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> mostrarFechaHoraActual());
            }
        }, 0, 60000); // Actualizar cada minuto (60000 milisegundos)
    }

    private void mostrarFechaHoraActual() {
        // Obtener la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();
        // Formatear la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        // Mostrar en la Label
        lbl_fecha_hora.setText("Fecha y hora actual: " + formattedDateTime);
    }

    private void actualizarNumeroElementos() {
        lbl_numero_elementos.setText("Número de elementos: " + datosSet.size());
    }

    private String getLocalIPAddress() throws UnknownHostException, SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        }
        throw new UnknownHostException("No se pudo obtener la dirección IP local");
    }
}
