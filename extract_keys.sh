#!/bin/bash

db5.3_dump -p -f dump /tmp/my_db/db_table.db
head -100 dump >> short_dump
