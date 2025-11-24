import React from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

// Înregistrați componentele Chart.js necesare
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const EnergyChart = ({ data, deviceName, selectedDate }) => {

  if (!data || data.length === 0) {
    return <p className="text-center mt-4 text-warning">No consumption data available for {deviceName} on {selectedDate}</p>;
  }

  // Mapează datele preluate în formatul Chart.js
  const chartData = {
    // Folosim ora (item.hour) pentru etichetele X. Convertim ora 0, 1, ... în șir de caractere.
    labels: data.map(item => `${item.hour}:00`),
    datasets: [
      {
        label: 'Energy Consumption (kWh)',
        // Folosim consumul agregat (item.consumption)
        data: data.map(item => item.consumption),
        backgroundColor: 'rgba(52, 152, 219, 0.8)',
        borderColor: 'rgba(52, 152, 219, 1)',
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: `Hourly Energy Consumption for ${deviceName} on ${selectedDate}`,
        font: {
          size: 18
        }
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: 'Hour of Day (End of Interval)',
          font: { weight: 'bold' }
        },
      },
      y: {
        title: {
          display: true,
          text: 'Consumption (kWh)',
          font: { weight: 'bold' }
        },
        beginAtZero: true,
      },
    },
  };

  return (
    <div style={{ maxWidth: '100%', margin: '20px auto' }}>
      <Bar data={chartData} options={options} />
    </div>
  );
};

export default EnergyChart;