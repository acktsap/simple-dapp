ret, ok = call("resetMarket"); assert(ok, ret)

ret, ok = call("issue","apart1", "owner1", 100); assert(ok, ret)
ret, ok = call("issue","apart2", "owner2", 100); assert(ok, ret)
ret, ok = call("issue","apart3", "owner3", 100); assert(ok, ret)

ret, ok = call("buy","apart2", "rich"); assert(ok, ret)
ret, ok = call("buy","apart3", "rich"); assert(ok, ret)

ret, ok = call("confirm", "apart3", "owner3"); assert(ok, ret)