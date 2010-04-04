package org.givwenzen.parse;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyEditorParser implements MethodParameterParser {
  public static final String EDITOR_SEARCH_PATH = "bdd.parse";

  public PropertyEditorParser() {
    addToPropertyManagerSearchPath(EDITOR_SEARCH_PATH);
  }

  public boolean canParse(Class<?> paramerterType) {
    return getPropertyEditorFor(paramerterType) != null;
  }

  public Object parse(Object paramerter, Class<?> paramerterType) throws Exception {
    PropertyEditor propertyEditor = getPropertyEditorFor(paramerterType);
    propertyEditor.setAsText(paramerter.toString());
    return propertyEditor.getValue();
  }

  public static PropertyEditor getPropertyEditorFor(Class<?> paramerterType) {
    return PropertyEditorManager.findEditor(paramerterType);
  }

  public static void addToPropertyManagerSearchPath(String editorSearchPath) {
    List<String> path = new ArrayList<String>(Arrays.asList(PropertyEditorManager.getEditorSearchPath()));
    for (String pathItem : path) {
      if (pathItem.equals(editorSearchPath))
        return;
    }
    path.add(editorSearchPath);
    PropertyEditorManager.setEditorSearchPath(path.toArray(new String[path.size()]));
  }

}
