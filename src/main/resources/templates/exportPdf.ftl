<!DOCTYPE html>
<head>
    <style>
        <#include "css/pdfStyle.css">
    </style>
    <title>Export PDF</title>
</head>
<html>
<body>
    <h1>Compte-rendu mensuel</h1>
    <h4>Utilisateur : ${user}</h4>
    <h4>Période : ${month} ${year?c}</h4>
    <table style="width:100%">
        <tr>
            <th>Projets</th>
            <#list dates as date>
               <th>${date.getDayOfMonth()}</th>
            </#list>
        </tr>
        <#list imputations as project, imputationDateMap>
            <tr>
                <td>${project.getNameProject()}</td>
                <#list datesString as date>
                    <#if imputationDateMap[date]??>
                        <td>${imputationDateMap[date][0].getDailyChargeImputation()}</td>
                    <#else>
                        <td></td>
                    </#if>
                </#list>
            </tr>
        </#list>

    </table>

</body>
</html>