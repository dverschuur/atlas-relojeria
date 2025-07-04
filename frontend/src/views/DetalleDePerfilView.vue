<template>
  <div class="detalle-container">
    <h2>Detalle del Perfil</h2>
    <ul>
      <li><strong>Nombre:</strong> {{ perfil.nombre }}</li>
      <li><strong>Cédula (ID):</strong> {{ perfil.id }}</li>
      <li><strong>Edad:</strong> {{ perfil.edad }}</li>
      <li><strong>Contraseña:</strong> {{ perfil.contrasena }}</li>
    </ul>

    <div class="botones-horizontales">
      <button v-if="!esAdmin" @click="eliminarPerfil" class="eliminar">Eliminar Perfil</button>
      <button @click="$emit('cerrar-sesion')" class="cerrar-sesion">Cerrar Sesión</button>
    </div>

    <p v-if="mensaje" class="success">{{ mensaje }}</p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'DetalleDePerfilView',
  props: {
    perfil: Object,
    esAdmin: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      mensaje: ''
    }
  },
  methods: {
    async eliminarPerfil() {
        const confirmar = confirm("¿Estás segura de que deseas eliminar este perfil?");
        if (!confirmar) return;

        try {
            await axios.delete(`http://localhost:8081/api/perfiles/eliminar/${this.perfil.nombre}`);
            this.mensaje = "Perfil eliminado exitosamente.";

            // Esperar 1.5 segundos y luego emitir el evento
            setTimeout(() => {
            this.$emit('perfil-eliminado');
            }, 1000);

        } 
        catch (err) {
            this.mensaje = "Error al eliminar el perfil.";
        }
}

  }
}
</script>

<style scoped>
.detalle-container {
  max-width: 400px;
  margin: 40px auto;
  padding: 20px;
  border: 1px solid #aaa;
  border-radius: 10px;
  background-color: #fefefe;
}

ul {
  list-style: none;
  padding: 0;
}
li {
  margin: 10px 0;
}

.success {
  color: green;
  font-weight: bold;
  margin-top: 15px;
}

.botones-horizontales {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

.botones-horizontales button {
  padding: 10px 15px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
}

.eliminar {
  background-color:#9c1c1c;
  color: white;
}

.cerrar-sesion {
  background-color: #9c1c1c;
  color: white;
}

</style>
