(function(){dust.register("force-local",body_0);function body_0(chk,ctx){return chk.section(ctx.get(["person"], false),ctx,{"block":body_1},null);}function body_1(chk,ctx){return chk.reference(ctx.getPath(true, ["root"]),ctx,"h").write(": ").reference(ctx.get(["name"], false),ctx,"h").write(", ").reference(ctx.get(["age"], false),ctx,"h");}return body_0;})();