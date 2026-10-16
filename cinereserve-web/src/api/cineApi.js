
import axios from "axios";

const cineApi = axios.create({
  baseURL: '/api',
})

export const obtenerFunciones = async () => {

  return respuesta.data;
};

export const obtenerAsientos = async (funcionId) => {
  
  return respuesta.data;
};

export const crearReserva = async (reserva) => {
  
  return respuesta.data;
};

export default cineApi;
