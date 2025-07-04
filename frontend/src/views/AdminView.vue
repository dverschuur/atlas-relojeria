<template>
  <div>
    <h2> Inventario de Relojes</h2>

    <button @click="mostrarFormulario = true" v-if="!mostrarFormulario">
      Agregar producto nuevo a inventario
    </button>

    <RegistrarProducto
        v-if="mostrarFormulario"
        @producto-agregado="onProductoAgregado"
        @cancelar="mostrarFormulario = false"
    />
    <br />
    <div class="inventario">
      <div v-for="reloj in relojes" :key="reloj.nombre" class="producto">
        <img :src="'http://localhost:8081/api/productos/img/' + reloj.imagen" :alt="reloj.nombre" />
        <h3>{{ reloj.nombre }}</h3>
        <p>Descripcion: {{ reloj.descripcion }}</p>
        <strong>Precio: ${{ reloj.precio }}</strong>
        <br /><br />
        <strong>Cantidad disponible: {{ reloj.cantidad }}</strong>
        <br />
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
import RegistrarProducto from './RegistrarProducto.vue'

export default {
  name: 'InventarioView',
  components: {
    RegistrarProducto
  },
  data() {
    return {
      relojes: [],
      mostrarFormulario: false
    }
  },
  mounted() {
    this.cargarInventario()
  },
  methods: {
    cargarInventario() {
      axios.get('http://localhost:8081/api/productos/inventario')
        .then(response => {
          this.relojes = response.data
        })
        .catch(error => {
          console.error('Error al cargar el inventario:', error)
        })
    },
    onProductoAgregado() {
      this.mostrarFormulario = false
      this.cargarInventario()
    }
  },
  props: {
    logueado: Boolean
  }
}
</script>

<style scoped>
.inventario {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  padding: 20px;
}
.producto {
  width: 220px;
  border: 1px solid #ccc;
  padding: 10px;
  text-align: center;
  border-radius: 10px;
  background-color:rgb(255, 255, 255);
}
.producto img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

button {
  margin-top: 10px;
  padding: 6px 10px;
  background-color:#0b7d59;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>