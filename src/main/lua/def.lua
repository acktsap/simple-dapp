local system = require("system");

----------------------------------------------------------
-- Get a current market
-- If current market is not defined, define a empty market
-- @return current market
----------------------------------------------------------
local function getMarket()
  local market = system.getItem("market")
  if market == nil then
    market = {}
    system.setItem("market", market)
  end
  return market
end

----------------------------------------------------------
-- Update market state
-- @param goodsName goods name
-- @param goods goods
----------------------------------------------------------
local function updateMarket(goodsName, goods)
  local market = getMarket()
  market[goodsName] = goods
  system.setItem("market", market)
end

----------------------------------------------------------
-- Reset a market
----------------------------------------------------------
function resetMarket()
  system.setItem("market", {})
end

----------------------------------------------------------
-- Issue a new goods
-- @param goodsName goods name
-- @param owner goods owner
-- @param price goods price
----------------------------------------------------------
function issue(goodsName, owner, price)
  local goods = {}
  goods["owner"] = owner
  goods["price"] = price
  goods["state"] = "Selling"
  goods["buyer"] = nil
  updateMarket(goodsName, goods)
end

----------------------------------------------------------
-- Make a buy request to the goods whose name is goodsName
-- State of goods must be 'Selling' and
-- owner of goods must be different with buyer
-- @param goodsName goods name
-- @param buyer goods buyer
----------------------------------------------------------
function buy(goodsName, buyer)
  local goods = getMarket()[goodsName]
  if goods ~= nil and goods["owner"] ~= buyer and goods["state"] == "Selling" then
    goods["state"] = "Requested"
    goods["buyer"] = buyer
    updateMarket(goodsName, goods)
  end
end

----------------------------------------------------------
-- Confirm a buy request
-- State of goods must be 'Requested' and
-- owner of goods must be identical with owner
-- @param goodsName goods name
-- @param owner goods owner
----------------------------------------------------------
function confirm(goodsName, owner)
  local goods = getMarket()[goodsName]
  if goods ~= nil and goods["owner"] == owner and goods["state"] == "Requested" then
    goods["state"] = "Sold"
    updateMarket(goodsName, goods)
  end
end

----------------------------------------------------------
-- Get a current market
-- @return current market
----------------------------------------------------------
function showMarket()
  return system.getItem("market")
end
