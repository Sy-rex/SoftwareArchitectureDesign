import psycopg2
from psycopg2.extras import execute_batch, RealDictCursor
from pymongo import MongoClient, errors as mongo_errors
import redis
from py2neo import Graph, Node, Relationship
from elasticsearch import Elasticsearch
from faker import Faker
import random
import datetime
import logging
import sys


def populate_postgres_data():
    try:
        conn = psycopg2.connect(**PG_CONFIG)
        cur = conn.cursor()
        
        execute_batch(cur, 
            """INSERT INTO student (student_number, fullname, email, id_group, redis_key) 
               VALUES (%s, %s, %s, %s, %s)
               ON CONFLICT (student_number) DO NOTHING""", 
            student_records, page_size=1000)

        conn.commit()
        cur.close()
        conn.close()
        logger.info("PostgreSQL: Данные успешно заполнены.")
    except Exception as e:
        logger.error("Ошибка при заполнении данных в PostgreSQL: %s", e)
        sys.exit(1)

def populate_mongodb():
    """Вставка в MongoDB без удаления коллекции"""
    try:
        client = MongoClient(MONGO_CONFIG['host'], MONGO_CONFIG['port'])
        db = client[MONGO_CONFIG['db']]
        
        for uni in universities:
            db.universities.update_one(
                {"id": uni["id"]},
                {"$setOnInsert": uni},
                upsert=True
            )
        logger.info("MongoDB: Данные успешно обновлены.")
    except mongo_errors.PyMongoError as e:
        logger.error("Ошибка MongoDB: %s", e)
        sys.exit(1)

def populate_redis(batch_size=1000):
    """Обновление Redis без очистки"""
    try:
        for stu in rows:
            r.hmset(key, student_data)
        logger.info("Redis: Данные обновлены.")
    except Exception as e:
        logger.error("Ошибка Redis: %s", e)
        sys.exit(1)

def populate_neo4j(batch_size=1000):
    """Добавление данных в Neo4j без удаления"""
    try:
        logger.info("Neo4j: Данные успешно добавлены.")
    except Exception as e:
        logger.error("Ошибка Neo4j: %s", e)
        sys.exit(1)

def populate_elasticsearch(batch_size=1000):
    """Обновление Elasticsearch без пересоздания индекса"""
    try:
        if not es.indices.exists(index=index_name):
            es.indices.create(index=index_name)
        
        logger.info("Elasticsearch: Данные обновлены.")
    except Exception as e:
        logger.error("Ошибка Elasticsearch: %s", e)
        sys.exit(1)

def main():
    logger.info("Начало работы скрипта.")
    
    populate_postgres_data()
    populate_mongodb()
    populate_redis()
    populate_neo4j()
    populate_elasticsearch()
    
    logger.info("Все данные успешно обновлены.")

if __name__ == "__main__":
    main()