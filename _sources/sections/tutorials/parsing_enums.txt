.. _tutorials_parsing_enums:

Parsing Enums
=============

Sometimes it is neccessary to parse the CSV data into an enum, which can be done with an :code:`EnumConverter`.

Example
~~~~~~~

Imagine we have a CSV file containing a vehicle, with a Name and a VehicleType. The VehicleType can only be a :code:`Car` or a :code:`Bike`.

::

    VehicleType;Name
    Car;Suzuki Swift
    Bike;A Bike

It useful to represent the VehicleType as an enumeration in our C# code. So first define the :code:`enum` in code:

.. code-block:: csharp

    private enum VehicleTypeEnum
    {
        Car,
        Bike
    }

Then define the class the results should be mapped to:

.. code-block:: java

    public class Vehicle {
    
        private VehicleTypeEnum vehicleType;
        private String name;
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getVehicleType() {
            return vehicleType;
        }
    
        public void setVehicleType(VehicleTypeEnum vehicleType) {
            this.vehicleType = vehicleType;
        }
    }

And now the mapping between the CSV File and the domain model has to be defined. For parsing the :code:`VehicleType`
a custom converter has to be used, which simply is a :code:`EnumConverter<VehicleTypeEnum>`.

.. code-block:: java

    private class CsvVehicleMapping extends CsvMapping<Vehicle>
    {
        public CsvVehicleMapping(IObjectCreator creator)
        {
            super(creator);
			
            mapProperty(0, VehicleTypeEnum.class, Vehicle::setVehicleType, new EnumConverter<>(VehicleTypeEnum.class));
            mapProperty(1, String.class, Vehicle::setName);
        }
    }

And then the CSV data can be parsed as usual.

.. _TinyCsvParser: https://github.com/bytefish/TinyCsvParser