console.log('Hello')

//const hwTest = Java.type("pt.up.fe.specs.crispy.test.HardwareInstanceTest");

//const testInstance = new hwTest();

const adderClass = Java.type("pt.up.fe.specs.crispy.coarse.Adder");

const adder = new adderClass(8);

adder.emit()
