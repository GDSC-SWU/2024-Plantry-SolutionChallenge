from fastapi import FastAPI, UploadFile
from google.cloud import vision
from google.oauth2 import service_account
import re
from PIL import Image
import pytesseract
from starlette.responses import JSONResponse
import os


#Service account key file path
service_account_key_file_path = "plantryocr-account.json"

#FastAPI app
app = FastAPI(form_data_limit=1024 * 1024 * 100)

#API endpoint
@app.post("/api/v1/parse-receipt")
async def parse_receipt_endpoint(image: UploadFile):
  #Read image file content as bytes
  image_bytes = await image.read()

  #Google Cloud Vision API request
  credentials = service_account.Credentials.from_service_account_file(
      service_account_key_file_path
  )
  client = vision.ImageAnnotatorClient(credentials=credentials)
  image = vision.Image(content=image_bytes)
  response = client.text_detection(image=image)

  #Extract text using Google Vision API
  google_vision_text = response.text_annotations[0].description

  #Tessdata directory configuration
  tessdata_dir_config = '--tessdata-dir ' + os.getcwd() + '/tessdata'

  #Extract text using Tesseract OCR
  with open("image.png", "wb") as f:
      f.write(image_bytes)
  tesseract_text = pytesseract.image_to_string(image=Image.open("image.png"), lang="eng+kor",
                                               config=tessdata_dir_config)

  #Combine both results
  text = google_vision_text + "\n" + tesseract_text

  #Extract food items and quantities
  food_info = get_food_info(text)

  #JSON response
  return JSONResponse({"data": food_info})

#Function to extract food items and quantities
def get_food_info(text):
  pattern = r"(.+?)\s+\d+\s+(\d+)"

  #Regular expression matches
  matches = re.finditer(pattern, text)

  #Parse extraction results
  food_info = []
  for match in matches:
    food_name = match.group(1)
    quantity = int(match.group(2))
    food_info.append({"food_name": food_name, "quantity": quantity})

  return food_info
