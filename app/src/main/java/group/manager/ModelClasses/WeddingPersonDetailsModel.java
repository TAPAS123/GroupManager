package group.manager.ModelClasses;

public class WeddingPersonDetailsModel  {

    public String name,fathername,age,mobileno;
    public byte[] imgByteArray;
    public WeddingPersonDetailsModel(String name,String fathername,String age, String mobileno, byte[] imgByteArray)
    {
         this.name = name;
         this.fathername = fathername;
         this.age = age;
         this.mobileno = mobileno;
         this.imgByteArray = imgByteArray;
    }
}
