#!/bin/bash

BASE_URL="http://localhost:8080"
TEST_COOP_ID="b2c3d4e5-0001-0001-0001-000000000001"

echo "=== Test: PUT /collectivities/{id}/informations ==="

echo ""
echo "--- Test 1: Happy path - Update informations successfully ---"
curl -s -X PUT "${BASE_URL}/collectivities/${TEST_COOP_ID}/informations" \
  -H "Content-Type: application/json" \
  -d '{"name": "Cooperative du Vallée", "number": 1001}' | jq .

echo ""
echo "--- Test 2: Not Found - Collectivity does not exist ---"
curl -s -X PUT "${BASE_URL}/collectivities/00000000-0000-0000-0000-000000000000/informations" \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Name", "number": 9999}' | jq .

echo ""
echo "--- Test 3: Bad Request - Name already used ---"
curl -s -X PUT "${BASE_URL}/collectivities/${TEST_COOP_ID}/informations" \
  -H "Content-Type: application/json" \
  -d '{"name": "Les Producteurs du Sud", "number": 9999}' | jq .

echo ""
echo "--- Test 4: Bad Request - Missing name ---"
curl -s -X PUT "${BASE_URL}/collectivities/${TEST_COOP_ID}/informations" \
  -H "Content-Type: application/json" \
  -d '{"number": 9999}' | jq .