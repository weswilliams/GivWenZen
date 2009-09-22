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

   public boolean canParse(Class paramType) {
      return getPropertyEditorFor(paramType) != null;
   }

   public Object parse(Object param, Class paramType) throws Exception {
      PropertyEditor propertyEditor = getPropertyEditorFor(paramType);
      propertyEditor.setAsText(param.toString());
      return propertyEditor.getValue();
   }

   public static PropertyEditor getPropertyEditorFor(Class paramType) {
      PropertyEditorManager.setEditorSearchPath(new String[] {EDITOR_SEARCH_PATH});
      return PropertyEditorManager.findEditor(paramType);
   }

   public static void addToPropertyManagerSearchPath(String editorSearchPath) {
      List<String> path = new ArrayList(Arrays.asList(PropertyEditorManager.getEditorSearchPath()));
      for (String pathItem : path) {
         if (pathItem.equals(editorSearchPath)) return;
      }
      path.add(editorSearchPath);
      PropertyEditorManager.setEditorSearchPath(path.toArray(new String[path.size()]));
   }

}
