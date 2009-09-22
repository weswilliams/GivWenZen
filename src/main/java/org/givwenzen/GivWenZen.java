package org.givwenzen;

public interface GivWenZen {

   Object given(String methodString) throws Exception;

   Object when(String methodString) throws Exception;

   Object then(String methodString) throws Exception;

   Object and(String methodString) throws Exception;

}
