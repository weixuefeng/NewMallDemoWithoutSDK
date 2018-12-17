# NewMallDemoWithoutSDK

# 1.add gradle dependency on your app build.gradle
```
implementation 'org.newtonproject.newpay.android.sdk:NewPaySDK:0.0.7'
```
# 2. init NewPayApi on application
```
NewPayApi.init(getApplication(), yourPrivateKey, $yourtransaferId);
```
# 3. request profile information
```
NewPayApi.requestProfileFromNewPay();
```
# 4. get profile information and sigmessage
```
String profile = data.getStringExtra("profile");
String sigMessage = data.getStringExtra("signature");
Log.e(TAG, "onActivityResult: " + data.toString() );
if(!TextUtils.isEmpty(profile)){
    ProfileInfo profileInfo = gson.fromJson(profile, ProfileInfo.class);
    cellphoneTextView.setText(profileInfo.cellphone);
    nameTextView.setText(profileInfo.name);
    newidTextView.setText(profileInfo.newid);
    Uri avatarUri = data.getData(); //your avatar information. if not,avatar is null.
    imageView.setImageURI(avatarUri);
}else{
    String error = data.getStringExtra("error");
    Log.e(TAG, "onActivityResult: " + error);
    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
}
if(!TextUtils.isEmpty(sigMessage)) {
    SigMessage sig = gson.fromJson(sigMessage, SigMessage.class);
    Log.e(TAG, "onActivityResult: " + sig.toString());  // you can verify sig message from here.
}
```
