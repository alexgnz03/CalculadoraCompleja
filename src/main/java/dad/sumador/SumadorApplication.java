package dad.sumador;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class SumadorApplication extends Application {

    private DoubleProperty operando1Real = new SimpleDoubleProperty();
    private DoubleProperty operando1Imaginario = new SimpleDoubleProperty();
    private DoubleProperty operando2Real = new SimpleDoubleProperty();
    private DoubleProperty operando2Imaginario = new SimpleDoubleProperty();
    private DoubleProperty resultadoReal = new SimpleDoubleProperty();
    private DoubleProperty resultadoImaginario = new SimpleDoubleProperty();
    private StringProperty operador = new SimpleStringProperty();

    private TextField operando1RealText;
    private TextField operando1ImaginarioText;
    private TextField operando2RealText;
    private TextField operando2ImaginarioText;
    private TextField resultadoText;
    private ComboBox<String> operadorCombo;

    @Override
    public void start(Stage primaryStage) throws Exception {
        operando1RealText = new TextField();
        operando1RealText.setPrefColumnCount(4);
        operando1ImaginarioText = new TextField();
        operando1ImaginarioText.setPrefColumnCount(4);
        operando2RealText = new TextField();
        operando2RealText.setPrefColumnCount(4);
        operando2ImaginarioText = new TextField();
        operando2ImaginarioText.setPrefColumnCount(4);
        resultadoText = new TextField();
        resultadoText.setPrefColumnCount(11);
        resultadoText.setDisable(true);

        operadorCombo = new ComboBox<String>();
        operadorCombo.getItems().addAll("+", "-", "*", "/");

        HBox root = new HBox(10,
            new VBox(operadorCombo),
            new VBox(
                new HBox(
                    operando1RealText,
                    new Label(" + "),
                    operando1ImaginarioText,
                    new Label(" i")
                ),
                new HBox(
                    operando2RealText,
                    new Label(" + "),
                    operando2ImaginarioText,
                    new Label(" i")
                ),
                new HBox(
                    resultadoText
                )
            ),
            new VBox(
                new Button("=") {
                    {
                        setOnAction(event -> onCalcularClick());
                    }
                }
            )
        );

        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Calculadora Compleja");
        primaryStage.setScene(scene);
        primaryStage.show();

        Bindings.bindBidirectional(operando1RealText.textProperty(), operando1Real, new NumberStringConverter());
        Bindings.bindBidirectional(operando1ImaginarioText.textProperty(), operando1Imaginario, new NumberStringConverter());
        Bindings.bindBidirectional(operando2RealText.textProperty(), operando2Real, new NumberStringConverter());
        Bindings.bindBidirectional(operando2ImaginarioText.textProperty(), operando2Imaginario, new NumberStringConverter());

        // Utilizamos una StringBinding para calcular el resultado como una cadena formateada
        StringBinding resultadoBinding = Bindings.createStringBinding(() -> {
            double real = resultadoReal.get();
            double imaginario = resultadoImaginario.get();
            return String.format("%.2f + %.2fi", real, imaginario);
        }, resultadoReal, resultadoImaginario);

        // Vinculamos el resultadoText a la StringBinding
        resultadoText.textProperty().bind(resultadoBinding);

        operador.bind(operadorCombo.getSelectionModel().selectedItemProperty());

        // Listener para cambios en la selección del operador
        operador.addListener((o, ov, nv) -> onOperadorChanged(nv));

        // Selección predeterminada en el ComboBox
        operadorCombo.getSelectionModel().selectFirst();
    }

    private void onOperadorChanged(String nv) {
        double a = operando1Real.get();
        double b = operando1Imaginario.get();
        double c = operando2Real.get();
        double d = operando2Imaginario.get();

        switch (nv) {
            case "+":
                resultadoReal.set(a + c);
                resultadoImaginario.set(b + d);
                break;
            case "-":
                resultadoReal.set(a - c);
                resultadoImaginario.set(b - d);
                break;
            case "*":
                resultadoReal.set(a * c - b * d);
                resultadoImaginario.set(a * d + b * c);
                break;
            case "/":
                double denominador = c * c + d * d;
                resultadoReal.set((a * c + b * d) / denominador);
                resultadoImaginario.set((b * c - a * d) / denominador);
                break;
        }
    }

    private void onCalcularClick() {
        // Esta función se llama al hacer clic en el botón "Calcular"
        // Llamamos a onOperadorChanged para realizar los cálculos
        onOperadorChanged(operador.get());
    }

    public static void main(String[] args) {
        launch(args);
    }
}