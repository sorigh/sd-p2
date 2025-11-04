import { HOST } from "../../commons/hosts";

export const getDevices = async () => {
    const response = await fetch(`${HOST.device_service}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json',
        },
    });
    
    if (!response.ok) throw new Error('Failed to fetch devices');
    const data = await response.json();  
    console.log("All devices:", data);
    return data;
};

export const getDeviceById = async (id) => {
    const response = await fetch(`${HOST.device_service}/${id}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json',
        },
    });
    if (!response.ok) throw new Error('Failed to fetch device');
    return response.json();
};

export const createDevice = async (deviceData) => {
    const response = await fetch(`${HOST.device_service}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(deviceData),
    });
    if (!response.ok) throw new Error('Failed to create device');
    return response.headers.get("Location");
};

export const updateDevice = async (id, deviceData) => {
    const response = await fetch(`${HOST.device_service}/${id}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(deviceData),
    });
    if (!response.ok) throw new Error('Failed to update device');
};

export const deleteDevice = async (id) => {
    const response = await fetch(`${HOST.device_service}/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
        },
    });
    if (!response.ok) throw new Error('Failed to delete device');
};

export const assignDevice = async (deviceId, ownerId) => {
    const response = await fetch(`${HOST.device_service}/${deviceId}/assign/${ownerId}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
        },
    });
    if (!response.ok) throw new Error('Failed to assign device');
};

export const unassignDevice = async (deviceId) => {
    const response = await fetch(`${HOST.device_service}/${deviceId}/unassign`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
        },
    });
    if (!response.ok) throw new Error('Failed to unassign device');
};


export const getMyDevices = async () => {
    const response = await fetch(`${HOST.my_device_service}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem("token")}`,
            'Content-Type': 'application/json',
        },
    });
    if (!response.ok) throw new Error('Failed to fetch devices');
    return response.json();
};

