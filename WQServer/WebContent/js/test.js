var param='timestamp=1418778262&nonce=461607798&msg_signature=febc52eff28f01e7123d92a93295585c528c7895';
param='timestamp=1418778270&nonce=1939906044&msg_signature=80ebd9e24e7eba274c268222047466843edc3405';
param='timestamp=1418778271&nonce=1385586097&msg_signature=b44e77db3bd13d2214d060c0555e7e62fe5624a5';
param='timestamp=1418778275&nonce=25208396&msg_signature=2c59330243eb515e930b6c435ec076461b279197';
param='timestamp=1418778294&nonce=318621780&msg_signature=8e71f1ecaa36eaeb688bf6bb9b9a3358205c975b';
param='timestamp=1418778304&nonce=989790800&msg_signature=afdd6cabde01ef0f7737268ed7cba4fb787a6e89';
param='timestamp=1418778304&nonce=719733651&msg_signature=b32dce090fdff01b27e91bcd9d3258b4826f560c';
param='timestamp=1418778324&nonce=181335283&msg_signature=0cda2716fe5228883fd5b2b1e2720d5f600b247f';
param='timestamp=1418778366&nonce=414758130&msg_signature=9d49d37552a7622d4418d74b13e5a7297be6a1ac';
var url="rest/wq/tool/coreJoin.do";
url="coreServlet";
$.ajax({
   type: "POST",
   url: url,
   data: param,
   success: function(msg){
    $("body").html(msg);
   }
});