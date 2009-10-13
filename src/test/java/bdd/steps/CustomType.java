package bdd.steps;

public class CustomType {

  private final String arg0;

  public CustomType(String arg0) {
    this.arg0 = arg0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CustomType other = (CustomType) obj;
    if (arg0 == null) {
      if (other.arg0 != null)
        return false;
    } else if (!arg0.equals(other.arg0))
      return false;
    return true;
  }

}
