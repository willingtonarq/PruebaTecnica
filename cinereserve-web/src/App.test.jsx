
import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import App from "./App";

import {
  obtenerFunciones,
  obtenerAsientos,
  crearReserva,
} from "./api/cineApi";

vi.mock("./api/cineApi", () => ({
  obtenerFunciones: vi.fn(),
  obtenerAsientos: vi.fn(),
  crearReserva: vi.fn(),
}));

const funcionesMock = [
  {
    id: 1,
    pelicula: "Interestelar",
    horario: "15:00",
    precio: 18000,
  },
];

const asientosMock = [
  { codigo: "A1", ocupado: false },
  { codigo: "A2", ocupado: true },
  { codigo: "A3", ocupado: false },
];

describe("CineReserve - pruebas de React", () => {
  beforeEach(() => {
    vi.clearAllMocks();

    obtenerFunciones.mockResolvedValue(funcionesMock);
    obtenerAsientos.mockResolvedValue(asientosMock);
  });

  it("muestra la película y su horario", async () => {
    render(<App />);

    expect(
      await screen.findByRole("heading", { name: "Interestelar" })
    ).toBeInTheDocument();

    expect(screen.getByText("Función: 15:00")).toBeInTheDocument();
  });

  it("muestra los asientos disponibles y deshabilita los ocupados", async () => {
    render(<App />);

    const asientoA1 = await screen.findByRole("button", {
      name: "Asiento A1, disponible",
    });

    const asientoA2 = screen.getByRole("button", {
      name: "Asiento A2, ocupado",
    });

    expect(asientoA1).toBeEnabled();
    expect(asientoA2).toBeDisabled();
  });

  it("permite seleccionar y deseleccionar un asiento", async () => {
    const user = userEvent.setup();
    render(<App />);

    const asiento = await screen.findByRole("button", {
      name: "Asiento A1, disponible",
    });

    await user.click(asiento);
    expect(asiento).toHaveAttribute("aria-pressed", "true");
    expect(screen.getByText(/Asientos: 1/)).toBeInTheDocument();

    await user.click(asiento);
    expect(asiento).toHaveAttribute("aria-pressed", "false");
    expect(screen.getByText(/Asientos: 0/)).toBeInTheDocument();
  });

  it("impide reservar si faltan el nombre o los asientos", async () => {
    const user = userEvent.setup();
    render(<App />);

    await screen.findByRole("button", {
      name: "Asiento A1, disponible",
    });

    await user.click(
      screen.getByRole("button", { name: "Confirmar reserva" })
    );

    expect(
      screen.getByText(
        "Ingresa tu nombre y selecciona al menos un asiento."
      )
    ).toBeInTheDocument();

    expect(crearReserva).not.toHaveBeenCalled();
  });

  it("envía los datos correctos y confirma una reserva exitosa", async () => {
    const user = userEvent.setup();

    crearReserva.mockResolvedValue({ id: 42 });

    // Tras reservar, la API devuelve los asientos actualizados.
    obtenerAsientos
      .mockResolvedValueOnce(asientosMock)
      .mockResolvedValueOnce([
        { codigo: "A1", ocupado: true },
        { codigo: "A2", ocupado: true },
        { codigo: "A3", ocupado: false },
      ]);

    render(<App />);

    await user.type(
      await screen.findByLabelText("Nombre del cliente"),
      "  Ana Pérez  "
    );

    await user.click(
      screen.getByRole("button", {
        name: "Asiento A1, disponible",
      })
    );

    await user.click(
      screen.getByRole("button", { name: "Confirmar reserva" })
    );

    await waitFor(() => {
      expect(crearReserva).toHaveBeenCalledWith({
        funcionId: 1,
        asientos: ["A1"],
        nombreCliente: "Ana Pérez",
      });
    });

    expect(
      await screen.findByRole("status")
    ).toHaveTextContent("¡Reserva #42 confirmada!");
  });

  it("muestra el error cuando el servidor rechaza la reserva", async () => {
    const user = userEvent.setup();

    crearReserva.mockRejectedValue({
      response: {
        status: 409,
        data: { detail: "El asiento A1 ya está ocupado." },
      },
    });

    render(<App />);

    await user.type(
      await screen.findByLabelText("Nombre del cliente"),
      "Carlos"
    );

    await user.click(
      screen.getByRole("button", {
        name: "Asiento A1, disponible",
      })
    );

    await user.click(
      screen.getByRole("button", { name: "Confirmar reserva" })
    );

    expect(
      await screen.findByRole("status")
    ).toHaveTextContent("El asiento A1 ya está ocupado.");
  });

  it("muestra un mensaje si falla la carga de funciones", async () => {
    obtenerFunciones.mockRejectedValueOnce(new Error("Error de conexión"));

    render(<App />);

    expect(
      await screen.findByRole("status")
    ).toHaveTextContent("No fue posible cargar las funciones.");
  });
});