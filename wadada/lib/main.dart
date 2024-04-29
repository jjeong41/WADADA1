import 'package:flutter/material.dart';
// import 'package:wadada/common/pages/mainpage.dart';
import 'package:wadada/screens/mypage/layout.dart';
import 'package:wadada/screens/mainpage/layout.dart';
import 'package:wadada/screens/singlemainpage/single_main.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';


void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await dotenv.load(fileName: 'assets/env/.env');
  final appKey = dotenv.env['APP_KEY'] ?? '';
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      home: SingleMain(),
    );
  }
}
