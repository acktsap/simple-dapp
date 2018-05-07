local system = require("system");
local json = require("json");

----------------------------------------------------------
-- Get a current market
-- If current market is not defined, define a empty market
-- @return current market
----------------------------------------------------------
local function getMarket()
  local market = system.getItem("market")
  if market == nil then
    system.setItem("market", {})
    registerEvents()
  end
  return market
end

----------------------------------------------------------
-- Register events
----------------------------------------------------------
local function registerEvents()
  local smartOracleEndpoint = "http://localhost:9000/event"
  local miningAddress = "141xLCiRBmAjBxGxRv3YwAvCTkTjYMLWtP"
  system.registEvent("ISSUE_EVENT", smartOracleEndpoint, miningAddress)
  system.registEvent("BUY_EVENT", smartOracleEndpoint, miningAddress)
  system.registEvent("CONFIRM_EVENT", smartOracleEndpoint, miningAddress)
end

----------------------------------------------------------
-- Update market state
-- @param uuid goods identifier
-- @param goods goods
----------------------------------------------------------
local function updateMarket(uuid, goods)
  local market = getMarket()
  market[uuid] = goods
  system.setItem("market", market)
end



----------------------------------------------------------
-- Issue a new goods
-- @param uuid goods identifier
-- @param goodsName goods name
-- @param price goods price
----------------------------------------------------------
function issue(uuid, goodsName, price)
  local goods = {}
  goods["goodsName"] = goodsName
  goods["price"] = price
  goods["owner"] = system.getSender() 
  goods["buyer"] = nil
  goods["state"] = "Selling"
  updateMarket(uuid, goods)

  local goodsName = goods["goodsName"]
  system.pushEvent("ISSUE_EVENT", {uuid, owner, goodsName})
end

----------------------------------------------------------
-- Make a buy request to the goods with correponding uuid
-- Owner of goods must be different with buyer and
-- state of goods must be 'Selling'
-- @param uuid goods identifier
----------------------------------------------------------
function buy(uuid)
  local goods = getMarket()[uuid]
  local buyer = system.getSender()
  if goods ~= nil and goods["owner"] ~= buyer and goods["state"] == "Selling" then
    goods["buyer"] = buyer
    goods["state"] = "Requested"
    updateMarket(uuid, goods)

    local goodsName = goods["goodsName"]
    system.pushEvent("BUY_EVENT", {uuid, buyer, goodsName})
  end
end

----------------------------------------------------------
-- Confirm a buy request
-- Owner of goods must be identical with sender and
-- state of goods must be 'Requested'
-- @param uuid goods identifier
----------------------------------------------------------
function confirm(uuid)
  local goods = getMarket()[uuid]
  local owner = system.getSender()
  if goods ~= nil and goods["owner"] == owner and goods["state"] == "Requested" then
    goods["state"] = "Sold"
    updateMarket(uuid, goods)

    local goodsName = goods["goodsName"]
    system.pushEvent("CONFIRM_EVENT", {uuid, owner, goodsName})
  end
end

----------------------------------------------------------
-- Get a current market
-- @return current market
----------------------------------------------------------
function showMarket()
  return system.getItem("market")
end

----------------------------------------------------------
-- Reset a market
----------------------------------------------------------
function resetMarket()
  system.setItem("market", nil)
end
