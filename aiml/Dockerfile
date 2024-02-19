FROM python:latest

COPY . .

RUN apt-get update \ 
    && apt-get -y install tesseract-ocr \
    && apt-get -y install tesseract-ocr-kor

RUN pip install -r requirements.txt

CMD uvicorn main:app --host=0.0.0.0 --port 8000 --reload