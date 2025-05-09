package hr.tvz.arydia.server.model;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Tile implements Serializable {


    private TileType tileType;
    private boolean active; //ovo cemo koristiti je li aktivan u open svijetu a za ostalo može li se proći kroz njega
    private transient Rectangle rect; //vjerojatno nece se moci serijalizirati ali vidjet cemo
    private transient Text text;
    private transient StackPane container;

    public Tile(TileType tileType, boolean active) {
        this.tileType = tileType;
        this.active = active;

        this.rect = new Rectangle(150, 150);
        this.text = new Text("");
        this.text.setFont(Font.font(14));
        this.text.setFill(Color.WHITE);

        this.container = new StackPane();
        this.container.getChildren().addAll(rect);
        this.container.getChildren().add(text);
        this.container.setAlignment(Pos.CENTER);

        setRectangleColor();
    }

    public void setText(String textContent) {
        this.text.setText(textContent);
    }

    private void setRectangleColor() {
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(2);
        if (active) {
            switch (tileType) {
                case OPEN_WORLD:
                    this.rect.setFill(Color.GREEN);
                    break;
                case BATTLE:
                    rect.setFill(Color.RED);
                    break;
                case EXPLORATION:
                    rect.setFill(Color.BLUE);
                    break;
                default:
                    rect.setFill(Color.GRAY);
                    break;
            }
        } else {
            rect.setFill(Color.GRAY);
//            switch (tileType) {
//                case OPEN_WORLD:
//                    break;
//                case BATTLE:
//                    rect.setFill(Color.DARKGRAY);
//                    break;
//                case EXPLORATION:
//                    rect.setFill(Color.BROWN);
//                    break;
//
//            }
        }
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
        setRectangleColor();
    }


}
