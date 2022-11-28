package ch.jacks.vaulture

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ch.jacks.vaulture.util.SessionUtil
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onPause() {
        super.onPause()

        SessionUtil.lastLoginDate = LocalDateTime.now().toString()
    }

    override fun onResume() {
        super.onResume()

        if (SessionUtil.importing) {
            SessionUtil.importing = false
            return
        }

        if (abs(
                Duration.between(
                    LocalDateTime.now(),
                    LocalDateTime.parse(SessionUtil.lastLoginDate)
                ).seconds
            ) > 5
        )
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .navController
                .navigate(R.id.RestartToLogin)
    }
}