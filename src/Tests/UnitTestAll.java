package Tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    Matrix3Test.class,
    Matrix4Test.class,
    Vector3Test.class,
    QuaternionTest.class
})
public class UnitTestAll {


}