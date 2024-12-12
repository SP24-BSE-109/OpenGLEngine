package Core.GUI;


import java.util.ArrayList;
import java.util.List;

public class GUIPanel extends GUIElement{

    private List<GUIElement> children = new ArrayList<>();

    public void addElement(GUIElement element) {
        children.add(element);
    }

    public void render() {
        if (!visible) return;
        for (GUIElement child : children) {
            child.render();
        }
    }

    @Override
    public void update(float deltaTime) {

    }
}
