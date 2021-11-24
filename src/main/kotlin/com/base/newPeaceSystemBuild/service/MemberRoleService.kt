package com.base.newPeaceSystemBuild.service

import com.base.newPeaceSystemBuild.repository.MemberRoleRepository
import com.base.newPeaceSystemBuild.util.Ut
import com.base.newPeaceSystemBuild.vo.ResultData
import com.base.newPeaceSystemBuild.vo.client.Family
import com.base.newPeaceSystemBuild.vo.client.FuneralHall
import com.base.newPeaceSystemBuild.vo.worker.Helper
import com.base.newPeaceSystemBuild.vo.worker.HelperTemp
import org.springframework.stereotype.Service


@Service
class MemberRoleService(
    private val memberRoleRepository: MemberRoleRepository,
    private val clientService: ClientService
) {


    fun insertMemberRole(introduce: String, memberId: Int, roleId: Int, roleCategoryId: Int) {
        memberRoleRepository.insertMemberRole(introduce, memberId, roleId, roleCategoryId)
    }

    fun getFuneralHallsByDepartment(department: String): ResultData {
        val funeralHalls = memberRoleRepository.getFuneralHallsByDepartment(department)

        val departmentDetails = mutableListOf<String>()


        // departmentDetail을 담은 후, 중복 제거 (distinct)
        for(funeralHall in funeralHalls){
            departmentDetails.add(funeralHall.departmentDetail)
        }

        // departmentDetail 부분만 따로 리스트에 담은 후, 중복 제거된 상태의 Json 데이터로 넘김
        return ResultData.from("S-1", "시군구를 불러오는데 성공했습니다.", "departmentDetails", departmentDetails.distinct())
    }

    fun getFuneralHallsByDepartmentDetail(departmentDetail: String): ResultData {
        val funeralHalls = memberRoleRepository.getFuneralHallsByDepartmentDetail(departmentDetail)

        val funeralHallNames = mutableListOf<String>()
        for(funeralHall in funeralHalls){
            funeralHallNames.add(funeralHall.name)
        }

        return ResultData.from("S-1", "장레식장 이름을 불러오는데 성공했습니다.", "funeralHallNames", funeralHallNames.distinct())
    }
    fun getFuneralHallByName2(name: String): FuneralHall{
        return memberRoleRepository.getFuneralHallByName(name)
    }
    fun getFuneralHallByName(name: String): ResultData {
        val funeralHall = getFuneralHallByName2(name)

        return ResultData.from("S-1", "장례식장 연락처를 불러오는데 성공했습니다.", "cellphoneNo", funeralHall.cellphoneNo)
    }

    fun getCoffinChisByName(coffinName: String): ResultData {

        val coffinChis = memberRoleRepository.getCoffinChisByName(coffinName)

        return ResultData.from("S-1", "관의 치 정보를 불러오는데 성공했습니다.", "coffinChis", coffinChis)
    }

    fun getCoffinSizesByChi(coffinChi: String): ResultData {

        val coffinSizes = memberRoleRepository.getCoffinSizesByChi(coffinChi)

        return ResultData.from("S-1", "관의 사이즈 정보를 불러오는데 성공했습니다.", "coffinSizes", coffinSizes)
    }

    fun getCoffinByAll(coffinName: String, coffinChi: String, coffinSize: String): ResultData {

        val coffin = memberRoleRepository.getCoffinByAll(coffinName, coffinChi, coffinSize)

        return ResultData.from("S-1", "coffinId를 불러오는데 성공했습니다.", "coffin", coffin)
    }

    fun getFuneralHallByAddress(destinationAddress: String): FuneralHall {
        return memberRoleRepository.getFuneralHallByAddress(destinationAddress)
    }

    fun getDepartmentDetailsByDepartment(department: String): List<String> {
        return memberRoleRepository.getDepartmentDetailsByDepartment(department)
    }

    fun getFuneralHallNamesByDepartmentDetail(departmentDetail: String): List<String> {
        return memberRoleRepository.getFuneralHallNamesByDepartmentDetail(departmentDetail)
    }

    fun doSelectHelper(clientId: Int, jsonStr: String): ResultData {

        val client = clientService.getClientById(clientId)
        val funeral = clientService.getFuneralByClientId(clientId)
        if(client == null || funeral == null){
            return ResultData.from("F-1", "잘못된 접근입니다.")
        }

        // 순수 Json 형식으로 정제한다.
        val str = jsonStr.drop(1).dropLast(5)
        val jsonStringsArr = str.split(",\"/\",")
        

        for(jsonStringArr in jsonStringsArr){
            val helpersTemp = mutableListOf<HelperTemp>()

            val str1 = jsonStringArr.drop(1) + ",["

            val objBits: MutableList<String> = str1.split("],[") as MutableList<String>


            // 마지막 String 배열은 항상 빈 값이므로 지운다.
            objBits.removeAt(objBits.size - 1)
            // 일감 별로 맞지 않는 부분 추가할 필요가 있다.


            // 히나의 일 단위를 helpersTemp 라는 배열에 담음
            for(objBit in objBits){
                val helperTemp = Ut.getObjFromJsonStr<HelperTemp>(objBit)

                helpersTemp.add(helperTemp)
            }
            // 배열에 담긴 하나의 일 단위를 DB에 저장 후, SMS 발송

            for(helperTemp in helpersTemp){
                // (1) helper의 일 단위에 대한 테이블 데이터 생성
                // (2) 1번에서 만든 테이블의 id를 helper 객체를 테이블에 담을 때 같이 넣어준다.
                // (3) SMS 발송
            }

        }

        
        return ResultData.from("S-1", "완료")

    }


}
