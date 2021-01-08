package com.example.demo.permission

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.contracts.contract

class RequestPermissionFragment : Fragment() {

    private var mPermissions : Array<out String>? = null
    private lateinit var mPermissionResult : (result : PermissionResult) -> Unit

    companion object {
        const val REQUEST_CODE = 1
        const val INTENT_PERMISSIONS = "intent_permissions"

        fun newInstance(vararg permissions : String, permissionResult : (result : PermissionResult) -> Unit) : RequestPermissionFragment {
            val bundle = Bundle()
            bundle.putStringArray(INTENT_PERMISSIONS, permissions)
            val fragment = RequestPermissionFragment()
            fragment.arguments = bundle
            fragment.mPermissionResult = permissionResult
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPermissions = it.getStringArray(INTENT_PERMISSIONS)
            if (mPermissions.isNullOrEmpty()) {
                this.mPermissionResult(PermissionResult())
            } else {
                mPermissions?.let { permissions ->
                    if (permissions.isNotEmpty()) {
                        requestPermissions(permissions, REQUEST_CODE)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            val result = PermissionResult()
            var success = true
            val failPermissions = mutableListOf<String>()
            for (i in grantResults.indices) {
                if (grantResults[i] == -1) {
                    success = false
                    failPermissions.add(permissions[i])
                }
            }
            if (success) {
                this.mPermissionResult(result)
            } else {
                result.success = false
                result.failPermission = failPermissions.toTypedArray()
                this.mPermissionResult(result)
            }
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

}