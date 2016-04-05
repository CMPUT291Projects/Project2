#!/bin/bash
rm -f dump
rm -f short_dump
db5.3_dump -p -f dump /tmp/edcarter/db_table.db
head -100 dump >> short_dump