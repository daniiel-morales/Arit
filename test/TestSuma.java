import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestSuma {
   
   @Test
   public void numConText() {
      
      // object result = call syntax('var1 = 4 + "hola"').parse()  
      assertEquals("4hola",null /* result */);
   }

   @Test
   public void textConNum() {
     
      // object result = call syntax('var2 = "hola" + 4.5').parse()  
      assertEquals("hola4.5",null /* result */);
   }
}