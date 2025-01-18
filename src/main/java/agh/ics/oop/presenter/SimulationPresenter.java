package agh.ics.oop.presenter;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import agh.ics.oop.*;
import agh.ics.oop.model.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationPresenter {

    @FXML
    private Label descriptionLabel;
    @FXML
    private ComboBox<String> growthVariantComboBox;
    @FXML
    private ComboBox<String> moveVariantComboBox;
    @FXML
    private ComboBox<String> mapVariantComboBox;
    @FXML
    private ComboBox<String> mutationVariantComboBox;
    @FXML
    private TextField widthInput;
    @FXML
    private TextField heightInput;
    @FXML
    private TextField plantEnergyInput;
    @FXML
    private GridPane mapGrid;

    private Simulation simulation;

    @FXML
    public void initialize() {
        growthVariantComboBox.getItems().addAll("FertileEquator", "OtherGrowthVariant");
        moveVariantComboBox.getItems().addAll("TotalPredestination", "OtherMoveVariant");
        mapVariantComboBox.getItems().addAll("Globe", "OtherMapVariant");
        mutationVariantComboBox.getItems().addAll("Random", "OtherMutationVariant");
    }

    @FXML
    private void onSimulationStartClicked() {
        Platform.runLater(() -> {
            try {
                if (growthVariantComboBox.getValue() == null ||
                        moveVariantComboBox.getValue() == null ||
                        mapVariantComboBox.getValue() == null ||
                        mutationVariantComboBox.getValue() == null) {
                    System.out.println("All options must be selected.");
                    return;
                }

                int width = Integer.parseInt(widthInput.getText());
                int height = Integer.parseInt(heightInput.getText());
                int plantEnergy = Integer.parseInt(plantEnergyInput.getText());

                GrowthVariant selectedGrowthVariant = growthVariantComboBox.getValue().equals("FertileEquator") ? new FertileEquator(width, height) : null;
                MoveVariant selectedMoveVariant = moveVariantComboBox.getValue().equals("TotalPredestination") ? new TotalPredestination() : null;
                AbstractWorldMap map = new GlobeMap(width, height, selectedGrowthVariant, selectedMoveVariant);
                MutationVariant selectedMutationVariant = mutationVariantComboBox.getValue().equals("Random") ? new FullRandomness() : null;
                map.setPlantEnergy(plantEnergy);

                SimulationConfig config = new SimulationConfig(map, selectedGrowthVariant, 5000, 100, selectedMoveVariant);
                SimulationEngine engine = new SimulationEngine(new Simulation(config));

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));
                BorderPane simulationRoot = loader.load();
                Stage simulationStage = new Stage();
                simulationStage.setTitle("Simulation");
                simulationStage.setScene(new Scene(simulationRoot));
                simulationStage.show();

                SimulationPresenter simulationPresenter = loader.getController();
                simulationPresenter.initializeSimulation(engine);

            } catch (NumberFormatException e) {
                System.out.println("Please enter valid numeric values for width, height, and plant energy.");
            } catch (IOException e) {
                System.out.println("Failed to load the simulation UI: " + e.getMessage());
            }
        });
    }

    public void initializeSimulation(SimulationEngine engine) {
        this.simulation = engine.getSimulation();
        startSimulation();
    }

    private void startSimulation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                simulation.runStep();
                renderMap();
            }
        };
        timer.start();
    }

    private void renderMap() {
        mapGrid.getChildren().clear();
        AbstractWorldMap map = simulation.getSimConfig().currentMap();

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                String cellContent = map.getCellContent(x, y);
                Label cell = new Label(cellContent);
                cell.setStyle("-fx-font-size: 14; -fx-alignment: center; -fx-border-color: black;");
                mapGrid.add(cell, x, y);
            }
        }
    }
}