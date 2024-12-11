from flask import Flask, request
import pandas as pd
import tensorflow as tf
import numpy as np
from sklearn.preprocessing import StandardScaler

model = tf.keras.models.load_model("models/nutrisee_model.h5")

app = Flask(__name__)

@app.route("/predict", methods=["GET"])
def predict():

  data_csv = pd.read_csv('data/data.csv')

  X = data_csv[['saturated-fat_100g', 'sugars_100g', 'fiber_100g', 'proteins_100g',
  'sodium_100g', 'fruits-vegetables-nuts-estimate-from-ingredients_100g', 'energy_kj']]


  # Normalize features
  scaler = StandardScaler()
  scaler.fit_transform(X)

  saturated_fat = float(request.form.get("saturated-fat_100g", default=0))
  sugar =  float(request.form.get("sugar_100g", default=0))
  fiber = float(request.form.get("fiber_100g", default=0))
  protein = float(request.form.get("proteins_100g", default=0))
  sodium = float(request.form.get("sodium_100g", default=0))
  fruit_vegetables = float(request.form.get("fruits-vegetables-nuts-estimate-from-ingredients_100g", default=0))
  energy = float(request.form.get("energy_kj", default=0))

  data = pd.DataFrame({
  'saturated-fat_100g': [saturated_fat],
  'sugars_100g': [sugar],
  'fiber_100g': [fiber],
  'proteins_100g': [protein],
  'sodium_100g': [sodium],
  'fruits-vegetables-nuts-estimate-from-ingredients_100g': [fruit_vegetables],
  'energy_kj': [energy]
  })

  data_scaled = scaler.transform(data)

  predictions = model.predict(data_scaled)

  # Mapping indeks ke label kelas
  classes = ['A', 'B', 'C', 'D', 'E']

  # Cari indeks dengan nilai maksimum
  predicted_indices = np.argmax(predictions, axis=1)

  # Ubah indeks menjadi label kelas
  predicted_labels = [classes[idx] for idx in predicted_indices]

  # Output probabilitas
  return predicted_labels

if __name__ == '__main__':
    app.run(debug=True)