import React, { useContext, useEffect, useState } from "react";
import Table from "../../commons/table";
import { AuthContext } from "../../context/authContext";
import { getMyDevices } from "../../device/api/device-api";

const MyDevicesPage = () => {
  const { user } = useContext(AuthContext);
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchDevices = async () => {
      try {
        const myDevices = await getMyDevices(user);
        setDevices(myDevices);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchDevices();
  }, [user]);

  const columns = [
    { Header: "ID", accessor: "id" },
    { Header: "Name", accessor: "name" },
    { Header: "Type", accessor: "type" },
    { Header: "Location", accessor: "location" },
    { Header: "Max Hourly Energy", accessor: "maximumHourlyEnergyConsumption" }
  ];

  if (loading) return <p>Loading devices...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  return (
    <div>
      <h2 className="text-center mb-4">My Devices</h2>
      <Table data={devices} columns={columns} pageSize={5} />
    </div>
  );
};

export default MyDevicesPage;
