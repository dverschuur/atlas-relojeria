<template>

  <div style="text-align: center;">
    <div v-if="!logueado" class="mensaje">
      <p><strong>Debes iniciar sesión</strong> para poder realizar una compra.</p>
    </div>
  </div>

  <div class="catalogo">
    <div v-for="reloj in relojes" :key="reloj.nombre" class="producto">
      <img :src="'http://localhost:8081/img/' + reloj.imagen" alt="Imagen del reloj" />
      <h3>{{ reloj.nombre }}</h3>
      <p>{{ reloj.descripcion }}</p>
      <strong>${{ reloj.precio }}</strong>
      <br />
      <button v-if="logueado" @click="$emit('comprar', reloj)">Comprar</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'CatalogoView',
  data() {
    return {
      relojes: []
    }
  },
  mounted() {
    axios.get('http://localhost:8081/api/productos/vista-cliente')
         .then(response => {
           this.relojes = response.data.filter(p => p.cantidad > 0)
         })
         .catch(error => {
           console.error("Error al cargar productos:", error)
         })
  },
  props: {
  logueado: Boolean
}
}
</script>

<style scoped>
.catalogo {
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
  border-radius: 8px;
  object-fit: cover;
}

.mensaje {
  display: inline-block;
  background-color: #fff3cd;
  color: #856404;
  border: 3px solid #ffeeba;
  border-radius: 8px;
  padding: 1px 1px;
  margin: 20px auto;
  text-align: center;
  line-height: 1.2;
  font-size: 15px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
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
