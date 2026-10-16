
import { useEffect, useState } from "react";
import {
  obtenerFunciones,
  obtenerAsientos,
  crearReserva,
} from "./api/cineApi";
import "./App.css";

function App() {
  const [funciones, setFunciones] = useState([]);
  const [funcionId, setFuncionId] = useState("");
  const [asientos, setAsientos] = useState([]);
  const [seleccionados, setSeleccionados] = useState([]);
  const [nombre, setNombre] = useState("");
  const [mensaje, setMensaje] = useState("");
  const [cargando, setCargando] = useState(false);

  useEffect(() => {
    obtenerFunciones()
      .then((data) => {
        setFunciones(data);
        if (data.length > 0) setFuncionId(String(data[0].id));
      })
      .catch(() => setMensaje("No fue posible cargar las funciones."));
  }, []);

  useEffect(() => {
    if (!funcionId) return;

    setSeleccionados([]);
    obtenerAsientos(funcionId)
      .then(setAsientos)
      .catch(() => setMensaje("No fue posible cargar los asientos."));
  }, [funcionId]);

  const alternarAsiento = (codigo) => {
    setSeleccionados((actuales) =>
      actuales.includes(codigo)
        ? actuales.filter((item) => item !== codigo)
        : [...actuales, codigo]
    );
  };

  const reservar = async () => {
    if (!nombre.trim() || seleccionados.length === 0) {
      setMensaje("Ingresa tu nombre y selecciona al menos un asiento.");
      return;
    }

    setCargando(true);
    setMensaje("");

    try {
      const reserva = await crearReserva({
        funcionId: Number(funcionId),
        asientos: seleccionados,
        nombreCliente: nombre.trim(),
      });

      setMensaje(`¡Reserva #${reserva.id} confirmada!`);
      setSeleccionados([]);
      setAsientos(await obtenerAsientos(funcionId));
    } catch (error) {
      setMensaje(
        error.response?.data?.detail ||
        error.response?.data?.message ||
        "No fue posible completar la reserva."
      );
      setAsientos(await obtenerAsientos(funcionId).catch(() => asientos));
    } finally {
      setCargando(false);
    }
  };

  const funcionActual = funciones.find(
    (funcion) => String(funcion.id) === funcionId
  );

  return (
    <main className="cine">
      <header className="encabezado">
        <div className="marca">CINE<span>RESERVE</span></div>
        <p>Tu próxima historia comienza aquí.</p>
      </header>

      <section className="panel">
        <h1>Reserva tus asientos</h1>
        <label htmlFor="funcion">Selecciona una función</label>
        <select
          id="funcion"
          value={funcionId}
          onChange={(e) => setFuncionId(e.target.value)}
        >
          {funciones.map((funcion) => (
            <option key={funcion.id} value={funcion.id}>
              {funcion.pelicula} · {funcion.horario} · $
              {Number(funcion.precio).toLocaleString("es-CO")}
            </option>
          ))}
        </select>

        {funcionActual && (
          <div className="detalle">
            <h2>{funcionActual.pelicula}</h2>
            <p>Función: {funcionActual.horario}</p>
            <p>
              Precio por asiento: $
              {Number(funcionActual.precio).toLocaleString("es-CO")}
            </p>
          </div>
        )}

        <div className="pantalla">PANTALLA</div>

        <div className="asientos">
          {asientos.map((asiento) => (
            <button
              key={asiento.codigo}
              type="button"
              disabled={asiento.ocupado}
              aria-pressed={seleccionados.includes(asiento.codigo)}
              className={[
                "asiento",
                asiento.ocupado ? "ocupado" : "",
                seleccionados.includes(asiento.codigo) ? "seleccionado" : "",
              ].join(" ")}
              onClick={() => alternarAsiento(asiento.codigo)}
              aria-label={`Asiento ${asiento.codigo}, ${
                asiento.ocupado ? "ocupado" : "disponible"
              }`}
            >
              {asiento.codigo}
            </button>
          ))}
        </div>

        <div className="leyenda">
          <span><i className="libre" /> Disponible</span>
          <span><i className="elegido" /> Seleccionado</span>
          <span><i className="lleno" /> Ocupado</span>
        </div>

        <div className="formulario">
          <label htmlFor="nombre">Nombre del cliente</label>
          <input
            id="nombre"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            placeholder="Escribe tu nombre"
          />

          <p className="resumen">
            Asientos: {seleccionados.length} · Total: $
            {(
              seleccionados.length * Number(funcionActual?.precio || 0)
            ).toLocaleString("es-CO")}
          </p>

          <button
            className="reservar"
            onClick={reservar}
            disabled={cargando || !funcionId}
          >
            {cargando ? "Procesando..." : "Confirmar reserva"}
          </button>

          {mensaje && <p role="status" className="mensaje">{mensaje}</p>}
        </div>
      </section>
    </main>
  );
}

export default App;
