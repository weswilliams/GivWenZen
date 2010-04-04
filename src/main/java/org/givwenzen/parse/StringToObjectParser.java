package org.givwenzen.parse;

import org.givwenzen.*;
import org.givwenzen.reflections.*;

import java.util.*;

public class StringToObjectParser {

   private List<MethodParameterParser> parsersInDesiredOrder = new ArrayList<MethodParameterParser>();

   public StringToObjectParser() {
      addCustomParsers(parsersInDesiredOrder);
      parsersInDesiredOrder.add(new StringParser());
      parsersInDesiredOrder.add(new PropertyEditorParser());
      parsersInDesiredOrder.add(new ArrayParser(new PropertyEditorParser()));
      parsersInDesiredOrder.add(new ValueObjectParser());
   }

   private void addCustomParsers(List<MethodParameterParser> parsersInDesiredOrder) {

      Reflections reflections = new ReflectionsBuilder()
         .basePackage("bdd.parse.")
         .subTypeScanner()
         .build();
      Set<Class<? extends MethodParameterParser>> classes = reflections.getSubTypesOf(MethodParameterParser.class);
      for (Class<? extends MethodParameterParser> aClass : classes) {
         try {
            parsersInDesiredOrder.add(aClass.newInstance());
         } catch (InstantiationException e) {
            throw new GivWenZenException(
               "Unable to instantiate " + aClass.getName() +
               ".  The usual cause of this is the class is an interface or abstract class.", e);
         } catch (IllegalAccessException e) {
            throw new GivWenZenException(
               "Unable to access the constructor for " + aClass.getName() +
               ".  The usual cause of this is the constructor is private or protected.", e);
         }
      }
   }

   public Object[] convertParamertersToTypes(Object[] paramerters, Class<?>[] paramerterTypes) throws Exception {
      List<Object> convertedParamerters = new ArrayList<Object>();
      for (int index = 0; index < paramerterTypes.length; index++) {
         convertedParamerters.add(convertStringToParamerterType(paramerters[index], paramerterTypes[index]));
      }
      return convertedParamerters.toArray(new Object[convertedParamerters.size()]);
   }

   private Object convertStringToParamerterType(Object paramerter, Class<?> paramerterType) throws Exception {
      for (MethodParameterParser parser : parsersInDesiredOrder) {
         if (parser.canParse(paramerterType)) {
            return parser.parse(paramerter, paramerterType);
         }
      }
      return paramerter;
   }
}
